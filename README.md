# SimpleTimerApp

## Description

//TODO

I tried to follow one *principle*:
> if possible move all functionality into the `ViewModel` or extract it into a `Service`.

## Core Module

The underlying `core` module was extracted and can be fount here: [ArchServices](https://github.com/DanielKnauf/ArchServices) 

Here it is received from [jitpack.io](https://jitpack.io) and included as a dependency: 
```
allprojects {
   repositories {
      maven { url 'https://jitpack.io' }
   }
}

dependencies {
   implementation 'com.github.DanielKnauf:archservices:master-SNAPSHOT'
}
```

## License
```
Copyright 2019 Daniel Knauf

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
