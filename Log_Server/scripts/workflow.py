import scripts.utils as util

IPADDR_MICROSERVICES = {"172.18.0.9": "aggr-ms", "172.18.0.1": "client"}

"""EXAMPLE
aggr-ms, 2018-05-27 15:11:24.548000, 2018-05-27 15:11:26.101000, 172.18.0.1, 53856, GET -> aggr-ms/playlists/songs/1, 862bbb69f6ff:5004
playlists-ms, 2018-05-27 15:11:24.678000, 2018-05-27 15:11:24.796000, 172.18.0.9, 49668, GET -> playlists-ms/playlists/songs/1, 3dd388ad0c98:5002
songs-ms, 2018-05-27 15:11:25.011000, 2018-05-27 15:11:25.073000, 172.18.0.9, 49676, GET -> songs-ms/songs, 598b9f73b150:5001
songs-ms, 2018-05-27 15:11:25.017000, 2018-05-27 15:11:25.136000, 172.18.0.9, 49680, GET -> songs-ms/songs, 598b9f73b150:5001
songs-ms, 2018-05-27 15:11:25.020000, 2018-05-27 15:11:25.115000, 172.18.0.9, 49684, GET -> songs-ms/songs, 598b9f73b150:5001
songs-ms, 2018-05-27 15:11:25.057000, 2018-05-27 15:11:25.292000, 172.18.0.9, 49694, GET -> songs-ms/songs, 598b9f73b150:5001"""

list_file = []
with open("../Data.csv") as f:
    f.readline()    # Remove headers
    for line in f:
        words = line.split(',')
        list_file.append([words[0], words[1], words[2], words[3], words[4], words[5], words[6]])
f.close()

with open("../workflow.txt", "w") as f:
    for row in list_file:
        f.write(IPADDR_MICROSERVICES[row[3][1:]] + " calls function " + row[5] + " from " +row[0] + "\n")



