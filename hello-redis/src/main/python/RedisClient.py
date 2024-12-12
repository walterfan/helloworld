from pytz import timezone
from datetime import datetime
import sys
import redis
from rediscluster import RedisCluster

from redis.client import Redis
from loguru import logger

logger.add(sys.stderr,
           format="{time} {message}",
           filter="client",
           level="INFO")
logger.add('logs/redis_client_{time:YYYY-MM-DD}.log',
           format="{time} {level} {message}",
           filter="client",
           level="ERROR")

class Apdex:
    _satisfied_count = 0.0
    _tolerating_count = 0.0
    _frustrated_count = 0.0

    def get_apdex_value(self):
        sum  = self._satisfied_count + self._tolerating_count + self._frustrated_count
        if sum == 0:
            return 1.0
        else:
            return (self._satisfied_count + self._tolerating_count/2)/sum

    def __str__(self):
        return "{},{},{}".format(self._satisfied_count, self._tolerating_count, self._frustrated_count)

class RedisClient:
    def __init__(self, connection_string, password=None):
        self.startup_nodes = []
        nodes = connection_string.split(',')
        for node in nodes:
            host_port = node.split(':')
            self.startup_nodes.append({'host': host_port[0], 'port': host_port[1]})

        self.password = password
        logger.debug(self.startup_nodes)
        self.redis_pool = None
        self.redis_instance = None
        self.redis_cluster = None

    def connect(self):
        if(len(self.startup_nodes) < 2):
            host = self.startup_nodes[0].get('host')
            port = self.startup_nodes[0].get('port')
            if self.password:
                self.redis_pool = redis.ConnectionPool(host=host, port=port, db=0)
            else:
                self.redis_pool = redis.ConnectionPool(host=host, port=port, password = self.password, db=0)

            self.redis_instance = Redis(connection_pool=self.redis_pool, decode_responses=True)
            return self.redis_instance
        #, skip_full_coverage_check=True
        self.redis_cluster = RedisCluster(startup_nodes=self.startup_nodes, password=self.password)
        return self.redis_cluster

def test_hashset(serviceName, apiName, timeSlot, redisHosts="localhost:6379"):
    logger.info("--- test_hashset ---")
    client = RedisClient(redisHosts)
    conn = client.connect()

    key_of_last_5min = "{}_{}".format(serviceName, timeSlot)

    conn.hsetnx(key_of_last_5min, apiName + "_satisfied_count", 0)
    conn.hsetnx(key_of_last_5min, apiName + "_tolerating_count", 0)
    conn.hsetnx(key_of_last_5min, apiName + "_frustrated_count", 0)
    conn.expire(key_of_last_5min, 300)

    conn.hincrby(key_of_last_5min, apiName + "_satisfied_count", 10)
    conn.hincrby(key_of_last_5min, apiName + "_tolerating_count", 20)
    conn.hincrby(key_of_last_5min, apiName + "_frustrated_count", 30)


def test_hashget(serviceName, timeslot, redisHosts="localhost:6379"):
    logger.info("--- test_hashget ---")

    client = RedisClient(redisHosts)
    conn = client.connect()

    key = "{}_{}".format(serviceName, timeslot)
    logger.info("key is " + key)
    values = conn.hgetall(key)
    apdex = Apdex()
    for key, value in values.items():
        logger.info("{}={}".format(key.decode("utf-8"), value.decode("utf-8")))
        sub_key = key.decode("utf-8")

        if sub_key.endswith('_satisfied_count'):
            apdex._satisfied_count = int(value)
        elif sub_key.endswith('_tolerating_count'):
            apdex._tolerating_count = int(value)
        elif sub_key.endswith('_frustrated_count'):
            apdex._frustrated_count = int(value)
    logger.debug(apdex)
    logger.info("apdex value is {}", apdex.get_apdex_value())

def main():
    current_time = datetime.now(timezone('UTC'))
    time_slot = int(current_time.timestamp() / 60)
    serviceName = "task_service"
    print("{}_{}".format(serviceName, time_slot))

    test_hashset("task_service", "create_task", time_slot)
    test_hashget(serviceName, time_slot)

if __name__ == "__main__":
    main()