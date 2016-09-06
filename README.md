# Developing manatee

1. Clone the repo: `git clone https://github.com/magnumresearch/manatee.git`

2. Follow the instructions at [JHipster-Installation][] to install Java, Gradle, Git, Node.js, Yeoman, Bower, Grunt and JHipster.

2. Install MySQL and create a new database called `myapp5`.

3. Import the application's MySQL database from `/Database/myapp5.sql` using command: `./mysql -u root -p myapp5 < /Users/fangzhousun/Documents/Projects/Cardiology Resource/Repo/manatee/Database/myapp5.sql`. Remember to replace the database file's path with your own path.

4. Run your application by typing  `./gradlew` in the manatee's root directory. If the command `./gradlew` leads to the following output: `Error: Could not find or load main class org.gradle.wrapper.GradleWrapperMain`, please run `gradle wrapper` and then `./gradlew` should work.

* This project's current name is `myapp5`. It was generated using JHipster, you can find documentation and help at [JHipster][].

* If you want to develop the backend in Eclipse, run command `gradle eclipse` in manatee's root directory. Then you will be able to import the project in Eclipse.

* The entities' configurations are saved in .json files in the `.jhipster` directory. If you want to modify a entity, first delete its table in the database and then run the corresponding command will re-generate the entity without the steps of answering questions provided by JHipster (Don't forget to manually update the database change log:http://jhipster.github.io/development/):

    `yo jhipster:entity referralSource`
    
    `yo jhipster:entity team`
    
    `yo jhipster:entity staff`
    
    `yo jhipster:entity patient`
    
    `yo jhipster:entity queue`




[JHipster]: https://jhipster.github.io/
[JHipster-Installation]: https://jhipster.github.io/installation.html
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Grunt]: http://gruntjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
