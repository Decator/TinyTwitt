# TinyTwitt

Martin Ars, Alexis Claveau, Alexis Loret, Maud Van Dorssen

# Results measurements

## 100 Followers

|          | Posting tweet | 10 Last Messages | 50 Last Messages | 100 Last Messages |
|----------|---------------|------------------|------------------|-------------------|
|  Average |    497 ms     |       187 ms     |       198 ms     |       198 ms      |   
| Variance |    18816      |       3864       |       2509       |        2330       |

## 1000 Followers

|          | Posting tweet | 10 Last Messages | 50 Last Messages | 100 Last Messages |
|----------|---------------|------------------|------------------|-------------------|
|  Average |    497 ms     |       194 ms     |       204 ms     |       232 ms      |   
| Variance |    23583      |       7545       |       2735       |        8059       |

## 5000 Followers

|          | Posting tweet | 10 Last Messages | 50 Last Messages | 100 Last Messages |
|----------|---------------|------------------|------------------|-------------------|
|  Average |    618 ms     |       140 ms     |       154 ms     |       170 ms      |   
| Variance |     2966      |        776       |       1119       |         394       |

## Hashtag 50 Last Messages

|          | 1000 Messages | 5000 Messages |
|----------|---------------|---------------|
|  Average |    705 ms     |    690 ms     |   
| Variance |     28368     |      23358    |

# App Engine Standard & Google Cloud Endpoints Frameworks & Java

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
    
0. Deploy the composite indexes:
		
		`gcloud app deploy src/main/webapp/WEB-INF/index.yaml`

0. Wait for the upload to finish.
