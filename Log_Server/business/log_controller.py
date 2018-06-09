import business.response_handling as RESP
import scripts.request_time_to_database as DB
import scripts.request_time_to_csv as CSV
import os


def hello_world():
    return RESP.response_200(message='Log_Server working!')


def create_log_entry(body):
    with open('EventSequence.txt', 'a') as file:
        file.write(body['message']+"\n")

    return RESP.response_200(message='Log written with success!')


def write_to_database():
    DB.run()
    return RESP.response_200()


def write_to_csv():
    CSV.run()
    return RESP.response_200()


def delete_files():
    os.remove('EventSequence.txt')
    os.remove('Data.csv')
    return RESP.response_200()
