<a href="https://seadev-studios.com/">
    <img src="https://www.seadev-studios.com/wp-content/uploads/2022/03/SEADEV_Logo_Atlassian_transparent.png" alt="SEADEV Studios" title="SEADEV Studios" align="right" height="60" />
</a>

# Keycloak Wordpress SPI

This Keycloak SPI provides a connector towards Wordpress. It allows Kecloak to add Wordpress as a User Federation, thus allowing the users to login through Keycloak with the same user account that they have on the Wordpress website.
The main benefit of interfacing with Wordpress through an SPI is that for the end user, he will just see the default keycloak login mask (compared to when using an Identity Provider) and everything else is handled in the background.

# Support and Help

Seems we are the first Keycloak Wordpress SPI. Like it that we made it open source? If you want to support us you can [Donate here](https://donate.stripe.com/00g2bg1jMf9k8ggbIJ).

Want to use this SPI commercially? Want us to create a build for you with your settings? Feel free to reach out to use on contact@seadev-studios.com and let us know.

# Getting started

A few easy steps to get started

## Configure Wordpress

Within Wordpress you will need there two plugins
[https://www.role-editor.com/](https://www.role-editor.com/)  
[https://github.com/WP-API/Basic-Auth](https://github.com/WP-API/Basic-Auth)

Then create a user and give it following roles:
-   create_users
-   edit_users
-   list_users

## Configure the SPI

Open the WordpressUserStorageProvider.java file and update the settings located on the top of the class.
For the Hash, this is the Hash for the BasicAuth, which is the Base64 encoded combination of "USERNAME:PASSWORD".

Then use maven to build the java archive.

## Deploying the SPI

Copy the java archive into the deployments folder of your keycloak instance. If you are using docker to deploy, this might be located at `/opt/jboss/keycloak/standalone/deployments`.
Restart Keycloak, go to the "User Federation" section and add a new provider named `wordpress-user-storage-spi`


# License

This Keycloak Wordpress SPI is licensed under the Apache License, Version 2.0
