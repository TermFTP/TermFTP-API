# TermFTP-API

The backend (server) for TermFTP built with Java

## Endpoints

The general request path is as follows: `<ip>:<port>/api/v1`

**Endpoints**

- [POST: `api/v1/register`](#register)
- [POST: `api/v1/login`](#login)
- [POST: `api/v1/createServer`](#create-server)
- [GET: `api/v1/serverGroups`](#server-groups)
- [POST: `api/v1/group`](#group-servers)
- [PUT: `api/v1/updateServer`](#update-server)
- [DELTE: `api/v1/removeServerFromGroup`](#remove-server-from-group)
- [DELETE: `api/v1/removeGroup`](#remove-server-group)
- [DELTE: `api/v1/removeServer`](#remove-server)
- [GET: `api/v1/confirm-account`](#confirm-account)
- [DELETE: `api/v1/deleteUser`](#delete-user)
- [DELETE: `api/v1/deleteUser/{id}`](#delete-user-as-admin)
- [POST: `api/v1/connection`](#save-connection)
- [GET: `api/v1/connections`](#get-connections)
- [Other Endpoints](#other-endpoints)

### Register

This endpoint is used to create a new user (register). Hereby, a verification email will be sent to the users email address. As soon as the user opens the link, the field `verified` changes to `true`.

- URI: `api/v1/register`

- Type: `POST`

- Body: a simplified version of the `User` object

  ```json
  {
      "username" : "user1",
      "password" : "a_pw",
      "email" : "some@gmail.com"
  }
  ```

  - Note: `email` must be unique!
  
- Response-Body: `DefaulRespone` with the newly created user

  ```json
  {
      "status": 200,
      "message": "Created User",
      "data": {
          "username": "user1",
          "email": "user1@gmail.com",
          "userID": "6293eed0-cbb7-4d7b-97df-49f45dc98528",
          "verified": false
      }
  }
  ```

  

### Login

This endpoint is used to login a user. After a successful login, this endpoint returns a unique Access-Token that required for many other endpoints.

- URI: `api/v1/login`

- Type: `POST`

- Headers:

  - `PC-Name` : `the-name-of-someones-pc`

- Body

  ```json
  {
      "username" : "user1",
      "password" : "a_pw"
  }
  ```

- Response-Body: `DefaulRespone` with the latest `Access-Token`

  ```json
  {
      "status": 200,
      "message": "AccessToken",
      "data": {
          "accessTokenID": {
              "token": "pzklzntrphjrabczvbxilzhoitggyaqi",
              "userID": "6293eed0-cbb7-4d7b-97df-49f45dc98528"
          },
          "validUntil": "2021-04-02",
          "pcName": "morpheus"
      }
  }
  ```

  

### Create Server

This endpoint is used to create a single server.

- URI: `api/v1/createServer`

- Type: `POST`

- Headers:

  - `Access-Token` : `<access-token>`

- Body

  ```json
  {
      "ip" : "127.0.0.3",
      "ftpPort" : "21",
      "sshPort" : "22",
      "name" : "local2",
      "username" : "admin",
      "password" : "admin"
  }
  ```

- Response-Body: `DefaulRespone` with the created `Server`

  ```json
  {
      "status": 200,
      "message": "Created Server",
      "data": {
          "serverID": "39957cfc-54e9-4347-939f-c976df1ff289",
          "ip": "127.0.0.3",
          "ftpPort": 21,
          "sshPort": 22,
          "lastConnection": null,
          "name": "local2",
          "username": "admin",
          "password": "admin"
      }
  }
  ```

  

### Server Groups

This endpoint is used to retrieve the whole tree of server groups (and sub-groups) with the containing servers PER USER.

- URI: `api/v1/serverGroups`

- Type: `GET`

- Headers:

  - `Access-Token` : `<access-token>`

- Response-Body: `DefaulRespone` with the tree of `ServerGroup`s  (list)

  ```json
  {
      "status": 200,
      "message": "Server-Groups",
      "data": [
          {
              "groupID": "839fad77-e6e5-4751-a6a2-803ca56f61af",
              "name": "default",
              "userID": "6293eed0-cbb7-4d7b-97df-49f45dc98528",
              "serverGroups": [],
              "server": [
                  {
                      "serverID": "39957cfc-54e9-4347-939f-c976df1ff289",
                      "ip": "127.0.0.3",
                      "ftpPort": 21,
                      "sshPort": 22,
                      "lastConnection": null,
                      "name": "local2",
                      "username": "admin",
                      "password": "admin"
                  }
              ]
          },
          {
              "groupID": "e1e81224-c73e-4e68-933e-657368f6f2d8",
              "name": "favourites",
              "userID": "6293eed0-cbb7-4d7b-97df-49f45dc98528",
              "serverGroups": [],
              "server": []
          }
      ]
  }
  ```

  

### Group Servers

This endpoint is used to create or change server groups. You can either create empty groups, sub-groups or groups with servers. Additionally this endpoints lets you change the name of the group or add further servers to it.

- URI: `api/v1/group`

- Type: `POST`

- Headers:

  - `Access-Token` : `<access-token>`

- Body

  - Create an empty group

    - **Note**: When creating a new group, don't send any `groupID` (otherwise the existing group with that ID will be renamed)

    ```json
    {
        "groupID" : null,
        "name" : "empty-group-01",
        "groups" : null,
        "servers" : null
    }
    ```

  - Add a sub-group

    - **Important** 
      - The sub group needs to be created first
      - The ID of the sub-group must not be the same as the *parent* `groupID`
    - `groupID` ... parent group
    - `groups` ... an array of sub-groups (which must already exist)

    ```json
    {
        "groupID" : "363855af-7aca-4ab0-951b-6411a0c345b6",
        "name" : null,
        "groups" : ["987855af-kd8454ab0-851c-7511a1c375c8"],
        "servers" : null
    }
    ```

  - <a name='createGroupWithServer'>Create a group with servers in it</a>

    - **Note**: When creating a new group, don't send any `groupID` (otherwise the existing group with that ID will be renamed)

    ```json
    {
        "groupID" : null,
        "name" : "work",
        "groups" : null,
        "servers" : ["39957cfc-54e9-4347-939f-c976df1ff289"]
    }
    ```

  - Rename a group

    - **Note**: When renaming a group, both the `groupID` and the `name` must be given

    ```json
    {
        "groupID" : "39957cfc-54e9-4347-939f-c976df1ff289",
        "name" : "work",
        "groups" : null,
        "servers" : ["39957cfc-54e9-4347-939f-c976df1ff289"]
    }
    ```

    

- Response-Body: `DefaulRespone` with the created/changed `ServerGroup` 

  ```json
  {
      "status": 200,
      "message": "Server-Group",
      "data": {
          "groupID": "363855af-7aca-4ab0-951b-6411a0c345b6",
          "name": "work",
          "userID": "6293eed0-cbb7-4d7b-97df-49f45dc98528",
          "serverGroups": [],
          "server": [
              {
                  "serverID": "39957cfc-54e9-4347-939f-c976df1ff289",
                  "ip": "127.0.0.3",
                  "ftpPort": 21,
                  "sshPort": 22,
                  "lastConnection": null,
                  "name": "local2",
                  "username": "admin",
                  "password": "admin"
              }
          ]
      }
  }
  ```

  - this is a response of [Create a group with servers in it](#createGroupWithServer)



### Update Server

This endpoint is used to update a single server.

- URI: `api/v1/updateServer`

- Type: `PUT`

- Headers:

  - `Access-Token` : `<access-token>`

- Body: the updated `Server` object

  ```json
  {
      "serverID" : "39957cfc-54e9-4347-939f-c976df1ff289",
      "ip" : "127.0.0.11",
      "ftpPort" : 21,
      "sshPort" : 22,
      "username" : "admin",
      "password" : "nowthereisastrongerpw"
  }
  ```

- Response-Body: `DefaulRespone` with the updated `Server`

  ```json
  {
      "status": 200,
      "message": "Updated Server",
      "data": {
          "serverID": "39957cfc-54e9-4347-939f-c976df1ff289",
          "ip": "127.0.0.11",
          "ftpPort": 21,
          "sshPort": 22,
          "lastConnection": null,
          "name": null,
          "username": "admin",
          "password": "nowthereisastrongerpw"
      }
  }
  ```

  

### Remove Server From Group

This endpoint is used to remove a single `Server`from a `ServerGroup`

- URI: `api/v1/removeServerFromServerGroup`

- Type: `DELETE`

- Headers:

  - `Access-Token` : `<access-token>`

- Request-Params:

  - `serverID` : `<id-of-the-server>`
  - `groupID` : `<id-of-the-group>` 
  
- Response-Body: `DefaulRespone` with status

  ```json
  {
      "status": 200,
      "message": "Removed 1 server from serverGroup.",
      "data": true
  }
  ```
  
  

### Remove Server Group

This endpoint is used to remove a whole`ServerGroup`

- URI: `api/v1/removeGroup`

- Type: `DELETE`

- Headers:

  - `Access-Token` : `<access-token>`

- Request-Params:

  - `groupID` : `<id-of-the-group>` 
  
- Response-Body: `DefaulRespone` with status

  ```json
  {
      "status": 200,
      "message": "Removed 1 serverGroup.",
      "data": true
  }
  ```

  

### Remove Server

This endpoint is used to remove a single `Server`

- URI: `api/v1/removeServer`

- Type: `DELETE`

- Headers:

  - `Access-Token` : `<access-token>`

- Request-Params:

  - `serverID` : `<id-of-the-server>` 

- Response-Body: `DefaulRespone` with status

  ```json
  {
      "status": 200,
      "message": "Removed 1 server.",
      "data": true
  }
  ```

  
### Confirm Account

This endpoint is used to remove a single `Server`

- URI: `api/v1/confirm-account`

- Type: `GET`

- Headers:

  - `Access-Token` : `<access-token>`

- Request-Params:

  - `token` : `<confirmation-token>` 

- Response-Body: `DefaulRespone` with status

  ```json
  {
      "status": 200,
      "message": "Verification",
      "data": true
  }
  ```



### Delete User

This endpoint is used to delete a single `User`

- URI: `api/v1/deleteUser`

- Type: `DELETE`

- Headers:

  - `Access-Token` : `<access-token>`

- Response-Body: `DefaulRespone` with status

  ```json
  {
      "status": 200,
      "message": "Deleted User",
      "data": 1
  }
  ```



### Delete User As Admin

This endpoint also deletes a user whereas it should only be used for debugging

- URI: `api/v1/deleteUser/{id}`

- Type: `DELETE`

- Headers:

  - `Access-Token` : `<access-token>`
    - Temp root AT
  
- Path Variables

  - `id`: the id of the user to be deleted

- Response-Body: `DefaulRespone` with status

  ```json
  {
      "status": 200,
      "message": "Deleted Users",
      "data": 1
  }
  ```



### Save Connection

This endpoint saves a connection (aka `HistoryItem`)

- URI: `api/v1/connection`

- Type: `POST`

- Headers:

  - `Access-Token` : `<access-token>`

- Request-Body:  `HistoryItem`

  ```json
  {
      "device" : "my private pc",
      "ip" : "127.0.0.25",
      "sshPort" : 22,
      "ftpPort" : 21,
      "username" : "admin"
  }
  ```

- Response-Body: `DefaultResponse` with the saved `HistoryItem`

  ```json
  {
      "status": 200,
      "message": "Saved HistoryItem (=Connection)",
      "data": {
          "historyItemID": {
              "userID": "2a2cac53-056b-4cac-af29-756fdfefcf21",
              "when": "2021-04-08T14:14:02.930337"
          },
          "device": "my private pc",
          "ip": "127.0.0.25",
          "deleted": false,
          "sshPort": 22,
          "ftpPort": 21,
          "username": "admin"
      }
  }
  ```

  

### Get Connections

This endpoint returns the complete history of a user's connections.

- URI: `api/v1/connections`

- Type: `GET`

- Headers:

  - `Access-Token` : `<access-token>`

- Response-Body: `DefaultResponse` with the history (list of `HistoryItem`)

  ```json
  {
      "status": 200,
      "message": "List of Connections (HistoryItems) aka 'vErLaUf'",
      "data": [
          {
              "historyItemID": {
                  "userID": "2a2cac53-056b-4cac-af29-756fdfefcf21",
                  "when": "2021-04-08T14:14:02.930337"
              },
              "device": "my private pc",
              "ip": "127.0.0.25",
              "deleted": false,
              "sshPort": 22,
              "ftpPort": 21,
              "username": "admin"
          },
          {
              "historyItemID": {
                  "userID": "2a2cac53-056b-4cac-af29-756fdfefcf21",
                  "when": "2021-04-08T14:19:33.108154"
              },
              "device": "my other pc",
              "ip": "127.0.0.26",
              "deleted": false,
              "sshPort": 22,
              "ftpPort": 21,
              "username": "admin"
          }
      ]
  }
  ```








### Other Endpoints

- `GET` : `api/v1/getUser/{id}`
- `GET` : `api/v1/getUsers`
- `GET` : `api/v1/settings`
- `POST` : `api/v1/settings`




