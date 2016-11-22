# PlayStartApp

This is a sample application using Play Framework (See tags https://github.com/yesnault/PlayStartApp/tags for download)

## Latest activator
This sample application is no longer maintained, but feel free to open a PR to add compatibility.

## Features
* Sign Up
* Sign In
* Reset password
* Settings Page
* Change email from Settings

## Application details
* No clear password stored in db
* Secure workflow to reset password
* Secure workflow to reset email
* I18n example (en, fr)
* Using Typesafe Plugin Mailer : https://github.com/typesafehub/play-plugins/tree/master/mailer
* Using Twitter Bootstrap 3 : http://twitter.github.com/bootstrap/
* Using Font-Awesome 4 : http://fortawesome.github.io/Font-Awesome/icons/
* Using Less and CoffeeScript
* Using a password strength checker

## Try
* Rename conf/email.conf.example in conf/email.conf and check it (smtp, etc...)
* Download Activator from http://www.playframework.org/
* Open a terminal in PlayStartApp directory and exec `activator run`
* Generate Scala Doc & Javadoc with exec `activator app-doc` (task app-doc add in Build.scala file)

## Activator
* See https://typesafe.com/activator/template/PlayStartApp

## Documentation
* Failing with Passwords (a presentation on issues in user authentication) : http://tersesystems.com/2012/02/17/failing-with-passwords
* Everything you ever wanted to know about secure password reset : http://www.troyhunt.com/2012/05/everything-you-ever-wanted-to-know.html

## Promotion
* You can vote for this application on http://www.playmodules.net/demo/14

## Licence
* BSD. See LICENSE file

## Contact
Twitter : @yesnault
