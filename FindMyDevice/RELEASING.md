# Releasing

This document describes how to publish a new release for FMD.

1. Update the `versionCode` and `versionName` in `app/build.gradle`.
2. Write a changelog and put it in `metadata/en-US/changelogs/{versionCode}.txt`
2. Commit and push
3. Tag the new release: `git tag v0.0.0` and push the tag: `git push --tags`
4. Create a new release on Gitlab: https://gitlab.com/Nulide/findmydevice/-/releases
5. Wait for F-Droid to pick it up!

## Testing

Before a new release, manually test the following things:

- Design:
  - Dark mode & light mode of new features
  - Edge-to-Edge on different navigation modes (gesture navigation, 3-button navigation)
- Basic commands on all transports: `fmd mypin help`
- Case insensitivity of trigger word and commands: `FMD locATE`
- FMD Server:
  - Registration, Logout, Login, Account deletion
  - Web: login, pushing commands
  - Background location upload
