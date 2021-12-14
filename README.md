# CleanURL

## Description

CleanURL can help you share a cleaned URL wherever you want without being tracked.

## Usage

Just choose this app in the `Android Sharesheet` when you share any text that includes a URL. Then this app will take some steps:

1. This app will extract the URL from the shared text.
2. If the URL extracted from Step 1 is a [Short URL](https://en.wikipedia.org/wiki/URL_shortening), this app will redirect the URL to get the final URL.
3. Make the URL from previous steps clean by deleting the query and fragment.

```
          userinfo       host      port
          ┌──┴───┐ ┌──────┴──────┐ ┌┴┐
  https://john.doe@www.example.com:123/forum/questions/?tag=networking&order=newest#top
  └─┬─┘   └───────────┬──────────────┘└───────┬───────┘ └───────────┬─────────────┘ └┬┘
  scheme          authority                  path                 query           fragment
```

## Development Environment

Android Studio Bumblebee | 2021.1.1 Beta 5

## License

[MIT License](https://github.com/hydrotho/CleanURL/blob/main/LICENSE)
