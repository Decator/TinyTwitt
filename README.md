# TinyTwitt

Martin Ars, Alexis Claveau, Alexis Loret, Maud Van Dorssen

# App Engine Standard & Google Cloud Endpoints Frameworks & Java

<a href="https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/GoogleCloudPlatform/java-docs-samples&page=editor&open_in_editor=appengine-java8/endpoints-v2-backend/README.md">
<img alt="Open in Cloud Shell" src ="http://gstatic.com/cloudssh/images/open-btn.png"></a>

## Build with Maven

### Adding the project ID to the sample API code

You must add the project ID obtained when you created your project to the
sample's `pom.xml` before you can deploy the code.

To add the project ID:

0. Edit the file `pom.xml`.

0. For `<endpoints.project.id>`, replace the value `YOUR_PROJECT_ID` with
your project ID.

0. Save your changes.

### Building the sample project

To build the project:

    mvn clean package

### Generating the openapi.json file

To generate the required configuration file `openapi.json`:

    mvn endpoints-framework:openApiDocs

### Deploying the sample API to App Engine

To deploy the sample API:

0. Invoke the `gcloud` command to deploy the API configuration file:

         gcloud endpoints services deploy target/openapi-docs/openapi.json

0. Deploy the API implementation code by invoking:

         mvn appengine:deploy

    The first time you upload a sample app, you may be prompted to authorize the
    deployment. Follow the prompts: when you are presented with a browser window
    containing a code, copy it to the terminal window.

0. Wait for the upload to finish.
