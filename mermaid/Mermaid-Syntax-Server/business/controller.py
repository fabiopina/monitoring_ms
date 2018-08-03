import business.response_handling as resp
import database.mysql_client as db
from flask import Response


def hello_world():
    return resp.response_200(message='Mermaid-Syntax-Server working!')


def get_mermaid_syntax():
    #rows = db.get_data()
    diagram = 'graph LR\nA[Square Rect] -- Link text --> B((Circle))\nA --> C(Round Rect)\nB --> D{Rhombus}\nC --> D\nD --> A'
    response = Response(diagram, mimetype='text/plain')
    response.headers.add('Access-Control-Allow-Origin', '*')
    return response







