```
mvn clean package
chmod 600 jmxremote.password
```

Out Of Memory Error Demo

```
# OOM of no heap space
source run_oom_demo.sh heap 100000000

# OOM of no native space
source run_oom_demo.sh native 100000000

# OOM of no meta space
source run_oom_demo.sh meta 100000000

# no OOM error
source run_oom_demo.sh hashmap 100000000 

# OOM of no heap space
source run_oom_demo.sh multimap 100000000
```
