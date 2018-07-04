import mysql.connector
import os
import time
import logging

cnx = None


def get_cnx():
    if cnx:
        return cnx

    host = os.getenv('DBADDRESS')
    database = os.getenv('DBDATABASE')
    user = os.getenv('DBUSER')
    password = os.getenv('DBPASSWORD')
    return connect(host, database, user, password)


def connect(host, database, user, password):
    global cnx
    failed = False
    while True:
        try:
            if failed:
                time.sleep(5)
            cnx = mysql.connector.connect(host=host, database=database, user=user, password=password, port=3306)
            logging.debug("{database} Connected to MySQL Database with success!")
            break
        except Exception:
            failed = True
            logging.debug("{database} MySQL Database is down. Reconnecting in 5 seconds ...")

    return cnx


def get_data():
    global cnx
    failed = False
    while True:
        try:
            if failed:
                time.sleep(5)

            cursor = get_cnx().cursor()
            query = ('''SELECT * FROM info''')
            cursor.execute(query)
            rows = cursor.fetchall()
            return rows

        except Exception:
            failed = True
            cnx = None
            logging.debug("{database} MySQL Database is down. Reconnecting in 5 seconds ...")



