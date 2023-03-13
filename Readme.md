
![image](https://ottaaproject.com/img/ottaa-project.svg)

# Welcome to OTTAA Project wiki #



OTTAA Project is an Alternative Augmentative Communication System, intended for people with speech impairments. It is a mobile, fast and effective tool that significantly improves the quality of life and facilitates social and labor integration.

We had already improved more than 40000 people lifestyle in 11 countries, our App helps people with Cerebral Palsy, Aphasia, Autism, Down Syndrome & mild ALS.
You can be part of this life-changer tech, join us in this social impact open source project.


[![](http://img.youtube.com/vi/zAL7yWxc-gU/0.jpg)](http://www.youtube.com/watch?v=zAL7yWxc-gU "Video")

# Project Info #

## Web Page
* [Web Page](https://ottaaproject.com)

## Resources

### Libraries
The libraries used are:

>* [volley](https://github.com/google/volley) - Network Requests

>* [Android-RateThisApp](https://github.com/kobakei/Android-RateThisApp) - App Rating

>* [lottie-android](https://github.com/airbnb/lottie-android) - Dynamic animations

>* [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) - Graphic Reports

>* [Glide](https://github.com/bumptech/glide) - Loading Images

>* [Lightweight-Stream-API](https://github.com/aNNiMON/Lightweight-Stream-API/blob/master/LICENSE) Java 7 lamba implementation

>* [SimpleNlg](https://github.com/simplenlg/simplenlg) - Natural Language Processing

>* [Android Support Library](https://developer.android.com) - Google's Support Library

>* [Android Image Cropper](https://github.com/CanHub/Android-Image-Cropper) - Can Hub Android Image Cropper

### Tools Needed


**Official Android developer tools**

![Android Studio](https://upload.wikimedia.org/wikipedia/commons/thumb/9/92/Android_Studio_Trademark.svg/128px-Android_Studio_Trademark.svg.png)
[Android Studio](https://developer.android.com/studio)

**Repository**

![Github](https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Octicons-mark-github.svg/64px-Octicons-mark-github.svg.png)
[Github](https://github.com)

**Pictograms**

![Arasaac](https://avatars2.githubusercontent.com/u/10613455?s=200&v=4)
[Araasac](http://arasaac.org/)

**Testing Platform**

![Testproject](https://blog.testproject.io/wp-content/themes/testprojectblog/img/t-plogo.png)
[Testproject](http://testproject.io)

![CircleCi](https://avatars.githubusercontent.com/u/1231870?s=100&v=4)
[CircleCi](http://app.circleci.com/)



## Documentation
* [Documentation](https://ottaaproject.com/javadoc)


# Information

## Contributing

### How to contribute
We would love your help. Before you start working however, please read and follow this guide.

#### Reporting Issues

Provide a lot of information about the bug. Mention the version of OTTAA Project and explain how the problem can be reproduced.

### Code Contributions



#### Create a pull request
In order to create a pull request is necessary

* Avoid file conflicts with the source code
* Should make a description about the characteristics to apply
* Should apply the pull request in the corresponding branch

|Branch|Description|
|---|---|
|Version| Main |
|Feature| Add new features |
|Hotfix|  Hot-fix about a version|
|Bugfix|  Bug-fix about a version|



### Documentation
#### Comments
* Comment documenting the source code are required.

* Comment a class explaining the purpose of that and how to implements if that required.

* Comment should be formatted as proper English sentences.
* use Javadoc documentation.

### Code

#### Duplication
* Don't copy-paste source code. Reuse it.

#### Import Libraries

* Sort by category.

|Category|Description|
|--------|-----------|
| Google | Library related to google |
| Android | Library related to android |
|Firebase | Library related to firebase api|
|Test |Library related to test app|
| Library | Library related to different apps|

* Sort by alphabetical order.

* Use Grandle level app
  Example :
```
#!xml
dependencies {
   implementation 'library'
}
```
#### Indentation


Switch case
```
#!java
Align by  such as these cases :

Switch(value){
    case 0:
       // Todo action here
    break;
    Default:
       // Todo default action here
    break;
}
```
If / else or else if
```
#!java

if(value.toString().equals("Hello")){
  //To do action here
}else if{
  // To do action here
} else{
  // Todo
}

```
**Remember:**

* The attributes of the class must be protected or private

* The Method of the class must be public, private or protected

* The class must be public or private

### Naming ###
**Name:** That must be transparent and representative about the action to show us.

**Class:** should be nouns in UpperCamelCase, with the first letter of every word capitalized.
example :

```
#!java
public class Json(){

}
```
**variable:** 	Local variables, instance variables, and class variables are also written in lowerCamelCase.

example :

```
#!java

String name =" Carl";
String fileName="json.txt";
```

**Constant:** Constants should be written in uppercase characters separated by underscores.

example :


```
#!java

public static final String CONSTANT_NAME=" fileName.txt";
```

#### Firebase index:

This is the Three in firebase :

```
#!code

index
├── Edad
├── email
├── Fotos
|    ├── nombre_foto
|    └── url_foto
├── FotosUsuario
├── Frases
├── Grupos
├── Juegos
├── Pago
├── Pictos
├── PrimeraUltimaConexion
└── Usuarios
```

## Code of Conduct

### OTTAA Project Open Source Code of Conduct

In order to work in the  OTTAA Project in a collaborative way and help our community grow we ask you to comply with the following code of conduct..

**Diversity makes us  grow :**  We truly believe that every user’s or developer’s age, gender, nationality, race or sexual orientation provide content based on a plurality of experiences and knowledge that contribute to the construction of a complete tool which reflects the real needs of potential users of the OTTAA Project.

**Debate enriches us :** As we consider that everyone can  contribute significantly to improving the software we seek to establish mutual respect among the members of the community, reaching a consensus among the developers and solving the problem in the best way possible.

It is necessary to comply with the following  guidelines in our conduct code:

* **Refraining from discriminating .**
* **Avoiding posting pornographic content.**
* **Refraining from publishing the user’s details or relevant  information.**
* **Refraining from making  heavy jokes.**
* **Avoiding insults**
* **Refraining from judging others on there religions or race**

### Reporting breaches to the code of conduct

In the case of any violation of our code of conduct, it should be reported as follows:
Share your contact details

* **Send a screenshot of the situation**
* **Explain the situation in as much detail as possible**
* **Send the email to the following address : support@ottaaproject.com**

After the  revision of the report, the team assigned to analyze the case will carry out the following actions:

* **Notify the user of the breach**
* **devise a way for the user to amend that attitude.**

the user can be expelled from the community in the following situation :

* **Repeated conduct**
* **Posting of pornographic content**

# Testing

## Master [![CircleCI](https://circleci.com/gh/OTTAA-Project/OTTAAProject/tree/master.svg?style=svg)](https://circleci.com/gh/OTTAA-Project/OTTAAProject/?master)

## Dev [![CircleCI](https://circleci.com/gh/OTTAA-Project/OTTAAProject/tree/dev.svg?style=svg)](https://circleci.com/gh/OTTAA-Project/OTTAAProject/?dev)





