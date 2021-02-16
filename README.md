# SVPlayer
New Players handler

### Description
This plugin gives a bunch of information about player connection, banishment and join rank

### Features
* Rank first join
* Send welcome broadcast
* Replace every player matching messages by a random other (containing new player's name)
* Give first and last join
* Give last known IP
* Give isMuted and isBanned with BanManager depedency
* Give amount of bans with BanManager depedency

### Commands
**Note** :

`<arg>` : required

`[arg]` : optional

#### Infos
##### Syntax
* `/infos [admin] <player>` : give all informations of a player

##### Arguments
* admin : to enable more information
* player : full name of the player (could be offline)

##### Permission
`svplayer.infos`

`svplayer.mod` (for admin argument)

#### Update
**Warning** This command will probably erase every information from Essentials (like home, powertools...)
##### Syntax
* `/svplayer update` : update the rank of every players
* `/svp update` : alias

##### Permission
`svplayer.admin`

#### Reload
##### Syntax
* `/svplayer reload` : update configuration from config file
* `/svp reload` : alias

##### Permission
`svplayer.admin`

### Other Permissions
* `svplayer.*` :
  * `svplayer.admin`
  * `svplayer.mod`
  * `svplayer.user`
