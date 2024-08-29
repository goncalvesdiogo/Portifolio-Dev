#!/bin/bash
aws --endpoint http://localhost:4566 secretsmanager create-secret --name /secret/servicestock --secret-string "{\"db_username\": \"root\", \"db_password\": \"password\"}" --region us-east-1

aws --endpoint http://localhost:4566 secretsmanager create-secret --name /secret/application --secret-string "{\"db_username\": \"root\", \"db_password\": \"password\"}" --region us-east-1
