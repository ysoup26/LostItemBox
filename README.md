# LostItemBox
로라 통신을 이용한 아두이노 프로젝트

## 오류해결

 1. 안드로이드
	- Task 'wrapper' not found in project ':project'
	```
	//Gradle.app의 android{}안에
	task wrapper(type: Wrapper) {
    	gradleVersion = '7.0'
	}
	//해결되면 해당 코드는 지우기
	```

	- Task 'prepareKotlinBuildScriptModel' not found in project ':app'.
	```
	//Gradle.app의 android{}안에
	tasks.register("prepareKotlinBuildScriptModel"){}
	//해결되면 해당 코드는 지우기
	```
	- Class나 Activity에 빨간 오류
	->오류가 나는 부분의 import를 지우고 다시 import

 2. 아두이노