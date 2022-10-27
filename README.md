<h1 align="center">LeetDroid - An android client for LeetCode</h1>

<img src ="app/src/main/res/drawable/app_logo.png" align="left" width="400" hspace="10" vspace="10">

LeetDroid is an Android client for LeetCode. It has most of the features that are provided in LeetCode that allow
you to see all the questions (around 1000+) along with their solutions, and discussion for every question. It also includes the following:

- General discussions for `interview experience`.
- New questions to solve daily that are from LeetCode's daily challenges.
- Contest timer and notifications to remind you to solve challenges.
- Your LeetCode profile is also integrated into the app enabling you to
view ranks, acceptance rates, contest history, ratings, and recent submissions too.
- It also provides random questions after every task from the user.

More features will be included in the app as
mentioned [here](https://github.com/cdhiraj40/LeetDroid/blob/main/README.md#future-scope)

> **Note**: You will need a LeetCode account to use LeetDroid. You can create a LeetCode account within LeetDroid.
<br>
Currently, the app supports only LeetCode users, it fetches data through a user's LeetCode profile. This might change in the future.

## Download

The first release of LeetDroid is out! Download it from [here](https://play.google.com/store/apps/details?id=com.cdhiraj40.leetdroid). Test it out and give us feedback if you find any bugs or if you need help.

## Why it was made

The idea came when I consecutively forgot about contests happening at LeetCode, and I could not find any app that was
sending notifications for a contest a day before. That's when I started researching, and then I made LeetDroid which does
not only have notifications but your whole LeetCode profile in your pocket.

## What it does

This project "LeetDroid" proposes a “one-stop for all LeetCode read-only work on Android” to keep track of all your
LeetCode activities, upcoming contests, and question lists (including their solution and discussions). Now you never have to worry about missing out on any contests from now on, the app will provide you with notifications 1 day and 30 minutes before a contest.

The app also shows daily LeetCode challenges that get updated every day at 5.30 AM with respect to your device's timezone. Furthermore,
LeetDroid provides info about your LeetCode profile right in your pocket with information from contest history to
recent submissions, rankings to no. of problems solved, and acceptance rate. We all get bored after searching for a
perfect question to practice, LeetDroid can also suggest random questions for you to practice :happy:

## Screenshots

<img src ="app/assets/leetdroid_collage.png" align="center">

## Demo Video

Play the video below to see a demo of the app

https://user-images.githubusercontent.com/72257135/198401128-5d00a46a-5763-41dc-a517-766ae33419fd.mp4

## 👇 Prerequisites

The following tools are required to run LeetDroid. Install them before you proceed.

- [Git](https://git-scm.com/downloads)
- [Android Studio](https://developer.android.com/studio)

If you don't have a LeetCode account, you can create one [here](https://LeetCode.com/accounts/signup/)

## 🛠️ Installation Steps

1. Fork [LeetDroid](https://github.com/cdhiraj40/LeetDroid.git/fork) repository.

2. Clone your forked copy of the project.

```bash
git clone https://github.com/<your_username>/LeetDroid.git
```

3. Create a new branch.

```bash
git checkout -b <your_branch_name>
```

4. Perform your desired changes to the code base.

5. Track your changes

```bash
git add . 
```

6. Perform a merge to sync your current branch with the upstream branch.

 ```bash
git fetch upstream
git merge upstream/main
```

7. : Commit your changes.

```bash
git commit -m "your commit message"
```

8. Push the committed changes in your feature branch to your remote repo.

```bash
git push -u origin <your_branch_name>
```

9. Create a Pull Request on GitHub.

---

## 👨‍💻 Contributing

- Any contributions you make to this project is **greatly appreciated**.

## Bug / Feature Request

If you find a bug in the app, kindly open an issue [here](https://github.com/cdhiraj40/LeetDroid/issues/new) to report it by
including a proper description of the bug and the expected result. We would also appreciate feature requests. If you feel like a certain feature is missing, feel free to create an issue to discuss with the maintainers.

## Functionality & Concepts used

The App has a very simple and interactive interface that can ease the process of using it. The following are a few Android concepts and tools that were used to build the app :

- [Android's MVVM Structure](https://developer.android.com/jetpack/guide)
- [Navigation Package](https://developer.android.com/reference/androidx/navigation/package-summary)
- [Navigation Component including set of principles](https://developer.android.com/guide/navigation/navigation-principles).
- [View Binding](https://developer.android.com/topic/libraries/view-binding)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) & [Room Database](https://developer.android.com/training/data-storage/room#kotlin)
- [Firebase Authentication](https://firebase.google.com/docs/auth)
- [Firebase Firestore](https://firebase.google.com/docs/firestore)
- [Lottie Animations](https://airbnb.io/lottie/#/)
- [Shared View Model](https://developer.android.com/codelabs/basic-android-kotlin-training-shared-viewmodel)
- [Alarm Receiver for daily notifications of new challenges](https://developer.android.com/training/scheduling/alarms)
- LeetCode's GraphQL API
- [Api for contests timings from ''Kontests''](https://kontests.net/)

## 🛡️ License

LeetDroid is licensed under the MIT License - see the [`LICENSE`](LICENSE) file for more information.

## Application Link

You can test the usability of the app with the latest development version for now by downloading it from [here](https://github.com/cdhiraj40/LeetDroid/blob/main/app/release/app-release.apk)

OR you can use the production release by obtaining it from the [Play Store](https://play.google.com/store/apps/details?id=com.cdhiraj40.leetdroid)

## Future Scope

~~I plan to implement the following features before releasing the first version on the Play Store.~~

- ~~Segregating Question list with topics, tags, etc.~~
- ~~Segregating general discussions with topics like `Interview-Question`, `Compensation`, `Career`, and `Study Guides` etc. including company tags.~~
- Showing different contests option along with differentiating attended & unattended contests.
- Improving the approach for pushing notifications.
- letting a guest user explore problems and all the features except user profiles etc.
