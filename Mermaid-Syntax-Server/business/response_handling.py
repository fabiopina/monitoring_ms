"""
    This script contains functions to format json responses to be sent to client
"""


# 200
# Standard response for successful HTTP requests.
def response_200(message=None, code=200):
    if message is None:
        message = {'message': 'OK'}
    return message, code


# 201
# The request has been fulfilled, resulting in the creation of a new resource.
def response_201(message=None, code=201):
    if message is None:
        message = {'message': 'Created'}
    return message, code


# 400
# The request could not be understood by the server due to malformed syntax.
def response_400(message=None, code=400):
    if message is None:
        message = {'message': 'Bad Request'}
    return message, code


# 401
# Authentication has failed or has not yet been provided.
def response_401(message=None, code=401):
    if message is None:
        message = {'message': 'Unauthorized'}
    return message, code


# 404
# The server has not found anything.
def response_404(message=None, code=404):
    if message is None:
        message = {'message': 'Not Found'}
    return message, code


# 409
# Indicates that the request could not be processed because of conflict in the request.
def response_409(message=None, code=409):
    if message is None:
        message = {'message': 'Conflict'}
    return message, code


# 500
# The server encountered an unexpected condition which prevented it from fulfilling the request.
def response_500(message=None, code=500):
    if message is None:
        message = {'message': 'Internal Server Error'}
    return message, code
