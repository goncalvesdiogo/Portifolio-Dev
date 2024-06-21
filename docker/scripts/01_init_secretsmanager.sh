#!/bin/bash
aws --endpoint http://localhost:4566 secretsmanager create-secret --name secret/mysql --secret-string "{\"username\":\"root\",\"password\":\"password\"}" --region us-east-1
aws --endpoint http://localhost:4566 secretsmanager create-secret --name secret/application --secret-string "{\"username\":\"root\",\"password\":\"password\"}" --region us-east-1
