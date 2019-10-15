val ktLint_version = "0.34.2"
val ktlint by configurations.creating

dependencies {
    ktlint("com.pinterest:ktlint:$ktLint_version")
}

task("ktLintCheck", JavaExec::class) {
    group = "kotlin verification"
    description = "Check Kotlin code style."
    main = "com.pinterest.ktlint.Main"
    classpath = configurations.getByName("ktlint")
    args("src/**/*.kt")
    // to generate report in checkstyle format prepend following args:
    // "--reporter=plain", "--reporter=checkstyle,output=${buildDir}/ktlint.xml"
    // see https://github.com/pinterest/ktlint#usage for more
}

tasks.named("check") {
    dependsOn(ktlint)
}

task("ktlintFix", JavaExec::class) {
    group = "formatting"
    description = "Fix Kotlin code style deviations."
    main = "com.pinterest.ktlint.Main"
    classpath = configurations.getByName("ktlint")
    args("-F", "src/**/*.kt")
}

