# SimpleTimerApp

This project started to try out patterns and functionalities of my first project after university in a smaller setting.
It's functionality is quite simple, you can set a time and two modes (once or onRepeat). After starting the timer it wil count down and make a sound (if the app is in the foreground) or show a notification (if the app is in the backgrond). 

Over time the implementation changed a lot, e.g. from a _classic_ android architecure approach to _mvvm_ or from _java_ to _kotlin_. While working on this project, one underlying _principle_ emerge: do not do any logic inside `Activity` or `Fragment`, place all functionality into the `ViewModel` or extract it into a `Service`.

## Core Module

The underlying `core` module was extracted and can be fount here: [ArchServices](https://github.com/DanielKnauf/ArchServices) 


## License
```
Copyright (C) 2019 Daniel Knauf

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
