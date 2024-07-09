#Mindex Java Coding Challenge Documentation
## Overview

This Spring Boot application will allow users to perform various actions on an Employee record including retrieving their Reporting Structure, updating their Compensation, and retrieving their Compensation.

##Reporting Structure
The endpoint "/reportingStructure/{id}" will retrieve the current Employee information such as employee ID, first name, last name, position, department, and direct reports. The service layer will fetch all of the direct reports for the desired employee, as well as any direct reports that they may have under them. Each of the direct reports will have their information fully described in the HTTP response body as seen below:
```json
{
    "employee": {
        "employeeId": "03aa1462-ffa9-4978-901b-7c001562cf6f",
        "firstName": "Ringo",
        "lastName": "Starr",
        "position": "Developer V",
        "department": "Engineering",
        "directReports": [
            {
                "employeeId": "62c1084e-6e34-4630-93fd-9153afb65309",
                "firstName": "Pete",
                "lastName": "Best",
                "position": "Developer II",
                "department": "Engineering",
                "directReports": null
            },
            {
                "employeeId": "c0c2293d-16bd-4603-8e08-638a9d18b22c",
                "firstName": "George",
                "lastName": "Harrison",
                "position": "Developer III",
                "department": "Engineering",
                "directReports": null
            }
        ]
    },
    "numberOfReports": 2
}
```

##Compensation
The endpoint "/compensation/{id}" will allow users to either perform an HTTP GET or an HTTP PUT, depending on the desired action.
To add new employee compensation information, the HTTP PUT request will need to include 2 things: the employee Id listed as a path variable, and the Request JSON Body with the following format:
```json
{
    "salary": 200000,
    "effectiveDate": "2024-07-07"
}
```
This functionality will find the employee in the database, and save the compensation to then be retrieved at a later time.
If successful, the endpoint will return a HTTP 201 Created response, as well as the full fleshed out Employee data.
The endpoint "compensation/{id}" HTTP GET will allow users to retrieve the compensation information for the desired employee. No Request JSON Body is required for this request. The result of this query will look similar to the JSON below:
```json
{
    "employee": {
        "employeeId": "62c1084e-6e34-4630-93fd-9153afb65309",
        "firstName": "Pete",
        "lastName": "Best",
        "position": "Developer II",
        "department": "Engineering",
        "directReports": null
    },
    "salary": 200000,
    "effectiveDate": "2024-07-07"
}
```

##Testing
This application utilizes Mockito to test both the service and the controller layer. This method of testing isolated each component of the application and creates a lightweight test suite that will not slow build times for large scale applications.