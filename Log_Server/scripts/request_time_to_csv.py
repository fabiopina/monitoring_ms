import scripts.utils as util

# EXAMPLE
# 2018-05-26 15:02:52.789 INCOMING GET 172.18.0.7 52370 http://zuul:4000/playlists-ms/playlists/songs/1 null null
# 2018-05-26 15:02:52.945 LEAVING GET 172.18.0.7 52370 http://zuul:4000/playlists-ms/playlists/songs/1 /playlists-ms http://55e6783ec759:5002/playlists/songs/1


def run():
    list_file = []
    with open("EventSequence.txt") as f:
        for line in f:
            words = line.split()
            list_file.append([util.get_date(words[0] + " " + words[1]), words[2],
                              words[3], words[4], words[5], words[6], words[7], words[8], words[9], "NO"])
    f.close()

    with open('Data.csv', 'w') as f:
        id = 1
        f.write('id,begin_request_time,end_request_time,request_time_in_milliseconds,source_ip,source_port,destiny_microservice,destiny_instance,destiny_ip,destiny_function\n')
        for row in list_file:
            if row[1] == 'INCOMING':
                for row_again in list_file:
                    if row_again[9] == 'NO' and util.is_the_response_request(row, row_again):
                        f.write(str(id) + "," + str(row[0]) + "," + str(row_again[0]) + "," + str(util.get_milliseconds(row_again[0] - row[0])) + "," + row_again[3] + "," + row_again[4] + "," + row_again[6][1:] + "," + util.get_instance(row_again[7]) + "," + row_again[8] + "," + util.get_function(row_again[2], row_again[5]) + "\n")
                        row_again[9] = 'YES'
                        id = id + 1
                        break
    f.close()















