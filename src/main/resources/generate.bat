REM
 REM Copyright 2021 The Billing Project, LLC
 REM
 REM The Billing Project licenses this file to you under the Apache License, version 2.0
 REM (the "License"); you may not use this file except in compliance with the
 REM License.  You may obtain a copy of the License at:
 REM
 REM    http://www.apache.org/licenses/LICENSE-2.0
 REM
 REM Unless required by applicable law or agreed to in writing, software
 REM distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 REM WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 REM License for the specific language governing permissions and limitations
 REM under the License.
 
 
java -classpath jooq-3.17.6.jar;^
jooq-meta-3.17.6.jar;^
jooq-codegen-3.17.6.jar;^
reactive-streams-1.0.3.jar;^
r2dbc-spi-0.9.1.RELEASE.jar;^
jakarta.xml.bind-api-3.0.0.jar;^
mysql-connector-j-8.0.31.jar;. ^
org.jooq.codegen.GenerationTool gen.xml