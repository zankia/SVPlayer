# SVPlayer
New Players handler

##English

###Description
This plugin gives a bunch of information about player connection, banishment and join rank

###Features
* Rank first join
* Send welcome broadcast
* Replace every player matching messages by a random other (containing new player's name)
* Give first and last join
* Give last known IP
* Give isMuted and isBanned with BanManager depedency
* Give amount of bans with BanManager depedency

###Commands
**Note** :

`<arg>` : required

`[arg]` : optional

####Infos
#####Syntax
* `/infos [admin] <player>` : give all informations of a player

#####Arguments
* admin : to enable more information
* player : full name of the player (could be offline)

#####Permission
`svplayer.infos`

`svplayer.mod` (for admin argument)

####Update
**Warning** This command will probably erase every information from Essentials (like home, powertools...)
#####Syntax
* `/svplayer update` : update the rank of every players
* `/svp update` : alias

#####Permission
`svplayer.admin`

####Reload
#####Syntax
* `/svplayer reload` : update configuration from config file
* `/svp reload` : alias

#####Permission
`svplayer.admin`

###Other Permissions
* `svplayer.*` :
  * `svplayer.admin`
  * `svplayer.mod`
  * `svplayer.user`


##Français

###Description
Ce plugin donne une flopée d'informations à propos de la connexion, le bannissement and le rang de connexion d'un joueur

###Fonctionnalités
* Rang de première connexion
* Envoie un message général de bienvenue
* Remplace chaque message de joueur correspondant par un autre aléatoirement (contenant le nom du nouveau joueur)
* Donne la première et la dernière connexion
* Donne la dernière IP connue
* Donne isMuted (estMuet) et isBanned (estBanni) avec une dépendance à BanManager
* Donne le nombre de bans avec une dépendance à BanManager

###Commandes
**Note** :

`<arg>` : requis

`[arg]` : optionnel

####Infos
#####Syntaxe
* `/infos [admin] <joueur>` : donne toutes les informations d'un joueur

#####Paramètres
* admin : pour activer plus d'informationsn
* player : nom complet du joueur (peut être hors ligne)

#####Permission
`svplayer.infos`

`svplayer.mod` (pour le paramètre admin)

####Update
**Attention** Cette command peut probablement effacer toutes les informations d'Essentials (comme les home, powertools...)
#####Syntaxe
* `/svplayer update` : met à jour le rang de chaque joueur
* `/svp update` : alias

#####Permission
`svplayer.admin`

####Reload
#####Syntaxe
* `/svplayer reload` : met à jour la configuration à partir du fichier config
* `/svp reload` : alias

#####Permission
`svplayer.admin`

###Autres Permissions
* `svplayer.*` :
  * `svplayer.admin`
  * `svplayer.mod`
  * `svplayer.user`
