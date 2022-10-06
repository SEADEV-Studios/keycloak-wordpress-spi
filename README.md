<a href="https://seadev-studios.com/">
    <img src="https://www.seadev-studios.com/wp-content/uploads/2022/03/SEADEV_Logo_Atlassian_transparent.png" alt="SEADEV Studios" title="SEADEV Studios" align="right" height="60" />
</a>

# Keycloak Wordpress SPI

This keycloak user storage SPI package, will allow you to select a 'wordpress' provider in the User Federation section of your keycloak installation.
Once you have enabled the plugin your application will be able to authenticate utilizing existing wordpress users via keycloak.

# Support and Help

If you want to support developments like this, please consider a donation: [Donate here](https://donate.stripe.com/00g2bg1jMf9k8ggbIJ).
In case you need support, please use the github issues to reach out.
Want to use this SPI commercially or want us to create a build for you with your settings? Feel free to reach out to use on contact@seadev-studios.com and let us know.

# Getting started
## Wordpress configuration

In order to be able to use the wordpress REST API to retrieve the necessary user information, you will need to install two plugins:
[https://www.role-editor.com/](https://www.role-editor.com/)  
[https://github.com/WP-API/Basic-Auth](https://github.com/WP-API/Basic-Auth)

After you have installed and activated those plugins you need to create a user and give it following roles:
-   create_users
-   edit_users
-   list_users
The Keycloak user storage SPI will authenticate with this user in order to manage users inside wordpress.
## User storage SPI configuration
Open the WordpressUserStorageProvider.java file and update the settings located on the top of the class.
The `wordpressAuthorizationHash` field is the hash needed for the BasicAuth, which is the Base64 encoded "USERNAME:PASSWORD" combination of the
management user you created in the previous step.
Then use maven to build the java archive.

`mvn clean install`

## User storage SPI deployment
Copy the java archive into the deployments folder of your keycloak instance. If you are using docker to deploy, this might be located at `/opt/jboss/keycloak/standalone/deployments`.
Restart Keycloak, go to the "User Federation" section and add a new provider named `wordpress`
# License
This Keycloak Wordpress user storage SPI is licensed under the Apache License, Version 2.0
