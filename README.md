# BPMN to Image Server

POST `locahost:8080` with multi-part upload.  Field name: `file`
or
POST `locahost:8080` with xml body


Query Params:

1. min_width: Int
1. min_height: Int
1. title: String
1. no_title: Boolean
1. no_footer: Boolean
1. scale: Int

# Docker

1. `docker build -t b2is .`
1. `docker run -p 8080:8080 --name bpmn-to-image-server b2is`

# Dev Notes:

1. Not all Alpine images work with puppeteer.  If you get "Failed to Launch Chrome" errors, make sure to test with other builds of the OS.
