# commands

## print all facts of servers

ansible local -m setup

ansible local -m setup -a 'filter=ansible_all_ip*'

# embed fact variable
* hostvars
* invertory_hostname
* group_names
* groups
* play_hosts
* ansible_version


