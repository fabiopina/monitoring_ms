import sys
import docker


def start(cli, event):
    """ handle 'start' events"""
    print(cli.inspect_container(event['id']))
    print(event['id'])


def stop(cli, event):
    """ handle 'stop' events"""
    print(event)


thismodule = sys.modules[__name__]
# create a docker client object that talks to the local docker daemon
cli = docker.Client(base_url='unix://var/run/docker.sock')
# start listening for new events
events = cli.events(decode=True)
# possible events are:
#  attach, commit, copy, create, destroy, die, exec_create, exec_start, export,
#  kill, oom, pause, rename, resize, restart, start, stop, top, unpause, update
for event in events:
    # if a handler for this event is defined, call it
    if hasattr(thismodule, event['Action']):
        getattr(thismodule, event['Action'])(cli, event)