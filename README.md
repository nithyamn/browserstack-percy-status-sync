# BrowserStack - Percy Status Sync
* Mark the test status on BrowserStack based on the Percy build state.

## Pre-requisites
* Export environment variables

For Unix-like or Mac machines:
````
export BROWSERSTACK_USERNAME=<YOUR_BROWSERSTACK_USERNAME> 
export BROWSERSTACK_ACCESS_KEY=<YOUR_BROWSERSTACK_ACCESS_KEY>
export PERCY_TOKEN=<PERCY_FULL_ACCESS_TOKEN>
````
For Windows:
````
set BROWSERSTACK_USERNAME=<YOUR_BROWSERSTACK_USERNAME> 
set BROWSERSTACK_ACCESS_KEY=<YOUR_BROWSERSTACK_ACCESS_KEY>
set PERCY_TOKEN=<PERCY_FULL_ACCESS_TOKEN>
````
* Requires Node 14+

## Steps to run
* `npm install`
* `sh src/runTest.sh`
