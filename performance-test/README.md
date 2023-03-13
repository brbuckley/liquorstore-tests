# performance-test

## Getting started

## Standard Usage

You can run it locally with your preferred IDE by running the **Engine** file, or using
maven `mvn gatling:test -Dgatling.simulationClass=<package>.<class>`

If no reports are generated, you can use maven if the folder is inside `/target/gatling` by `mvn gatling:test -DreportsOnly=<folder>`

## Configuration

You can configure the test parameters by sending extra environment variables appending them in the maven command
`-DparameterYouWant=value`.

**Available run configurations**

| Config                           | Default                               |               
|----------------------------------|---------------------------------------|
| distributed                      | 1                                     |
| success_rate                     | 99                                    |
| timeout                          | 1000                                  |
| ramp_down                        | 300                                   |
| ramp_up                          | 300                                   |
| request.body                     | '{"id":1}'                            |
| request.expected_status          | 200                                   |
| request.headers                  | '{"Content-Type":"application/json"}' |
| request.http_method              | GET                                   |
| request.url                      | http://127.0.0.1:8080                 |
| threshold.value                  | 100                                   |
| threshold.multiplier             | 0                                     |
| breakpoint.duration              | 2600                                  |
| breakpoint.function.inaccuracy   | 0075                                  |
| breakpoint.function.initial_load | 1                                     |
| breakpoint.tolerance             | 1                                     |
| spike.period                     | 300                                   |
| spike.repetition                 | 3                                     |
| spike.wait_time                  | 300                                   |
| stress.duration                  | 1200                                  |
