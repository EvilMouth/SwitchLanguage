# Switch Language Utils

[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html)
[ ![Download](https://api.bintray.com/packages/zyhang/maven/SwitchLanguage/images/download.svg) ](https://bintray.com/zyhang/maven/SwitchLanguage/_latestVersion)

<img width="216" height="384" src="gif/1.gif"/>

## Usage

### 重写Activity
```java
@Override
protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(SwitchLanguageUtils.configLanguage(newBase));
}
```

### 切换语言
```kotlin
SwitchLanguageUtils.startSwitchLanguage(Locale.CHINA, 1000)
```

## Installation

```groovy
implementation 'com.zyhang:switchlanguage:<latest-version>'
```