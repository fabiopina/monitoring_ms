import datetime


def get_date(string):
    """ Breaks the entire log string into a datetime object"""
    """ Input: 2018-05-10 22:38:38.671 -> datetime object"""
    words = string.split()

    # Need to cut the first two digits from the year
    string_date = words[0][2:] + " " + words[1]
    dt = datetime.datetime.strptime(string_date, "%y-%m-%d %H:%M:%S.%f")
    return dt


def is_the_response_request(first_list, second_list):
    """ List input example: [datetime.datetime(2018, 5, 26, 15, 2, 52, 3000), 'INCOMING', 'GET', '172.18.0.1', '38802',
                                        'http://172.18.0.5:4000/aggr-ms/playlists/songs/1', 'null', 'null', 'NO']"""
    if second_list[1] == 'LEAVING' and second_list[2] == first_list[2] and second_list[3] == first_list[3] and second_list[4] == first_list[4] and second_list[5] == first_list[5]:
        return True
    return False


def get_function(method, url):
    """ Method example: GET
        URL example: http://zuul:4000/playlists-ms/playlists/songs/1"""
    words = url.split(':')
    return method + " -> " + words[2][5:]


def get_instance(uri):
    """ Input: http://55e6783ec759:5002/playlists/songs/1"""
    words = uri.split('/')
    return words[2]


def get_milliseconds(timedelta):
    return timedelta.days * 86400000 + timedelta.seconds * 1000 + timedelta.microseconds / 1000






