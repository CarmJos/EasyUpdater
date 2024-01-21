```text
   ____                __  __        __     __         
  / __/__ ____ __ __  / / / /__  ___/ /__ _/ /____ ____
 / _// _ `(_-</ // / / /_/ / _ \/ _  / _ `/ __/ -_) __/
/___/\_,_/___/\_, /  \____/ .__/\_,_/\_,_/\__/\__/_/   
             /___/       /_/                          
```

README LANGUAGES [ [**English**](README.md) | [中文](README_CN.md)  ]

# EasyUpdater

[![version](https://img.shields.io/github/v/release/CarmJos/EasyUpdater)](https://github.com/CarmJos/EasyUpdater/releases)
[![License](https://img.shields.io/github/license/CarmJos/EasyUpdater)](https://www.gnu.org/licenses/lgpl-3.0.html)
[![workflow](https://github.com/CarmJos/EasyUpdater/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/EasyUpdater/actions/workflows/maven.yml)
[![CodeFactor](https://www.codefactor.io/repository/github/carmjos/EasyUpdater/badge)](https://www.codefactor.io/repository/github/carmjos/EasyUpdater)
![CodeSize](https://img.shields.io/github/languages/code-size/CarmJos/EasyUpdater)
![](https://visitor-badge.glitch.me/badge?page_id=EasyUpdater.readme)

**Easy _(to keep)_ updates!**

Advanced plugin and config updater, which can keep plugins up-to-dates and mixin specific configuration files.

## Features & Advantages

## Usages & Examples

Download the latest version of the EasyUpdater from 
the [releases](https://github.com/CarmJos/EasyUpdater/releases)
and put it in the folder of your application. 

Then, create a new file named `updates.yml` or any other name you like.

```yaml
# Import other update configs, which will be loaded before this file.
# All configs will be run in order.
import:
  - "<path-to-other-config>/general-updates.yml"

# Transfer files from the sources.
transfer:
  "task1":
    source: "<path-to-plugin-source>/ExamplePlugin-(.*)-SNAPSHOT.jar" # The source file path.
    target: "plugins/ExamplePlugin(.*).jar" # The target file path.
    options:
      rename: "ExamplePlugin-$1.jar"
  "task2":
    source: "<path-to-plugin-source>/some-files/"
    target: "libraries" # Target folder
    options:
      override: true # Override the files in the target folder.
      filter:
        - "*.log"
        - "*.log.gz"

# Mixin specific configuration files.
mixin:
  task1:
    schema: YAML # Optional, auto-detected by file suffix if not provided.
    source: "<path-to-source>/xxx.yaml"
    target: "plugins/some-plugin/config.yml"
  task2:
    source: "<path-to-source>/survival.properties"
    target: "server.properties"

```

Finally, run `java -jar EasyUpdater.jar [config-file-path]` to start the updater 
before your own application boot up.

## Support and Donation

If you appreciate this plugin, consider supporting me with a donation!

Thank you for supporting open-source projects!

Many thanks to Jetbrains for kindly providing a license for us to work on this and other open-source projects.

[![](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)](https://www.jetbrains.com/?from=https://github.com/ArtformGames/EasyUpdater)

## Open Source License

This project's source code is licensed under
the [GNU LESSER GENERAL PUBLIC LICENSE](https://www.gnu.org/licenses/lgpl-3.0.html).
 