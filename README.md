# Noticias sobre criptomoedas

> Repositório com a solução do desafio que para desenvolvimento de aplicativo para Android consumindo a API [News API] para exibir noticias sobre criptomoedas.

## Demo

![criptonews](https://raw.githubusercontent.com/pedrox-hs/crypto-news-app/master/demo/demo.gif)

## Requisitos

* IDE Android Studio >= 3.4.2
* Android SDK >= 19
* API Key da [News API]

## Setup

Deve ser configurada a propriedade do gradle chamada `news_api_key` com a API Key de [News API].
Para isso crie o arquivo `app/gradle.properties` com o seguinte conteúdo substituindo `YOUR_API_KEY_HERE` pela sua API Key:

```
news_api_key=YOUR_API_KEY_HERE
```

## Arquitetura

Arquitetura foi baseada em MVVM, buscando referência do Clean Architecture.

## Bibliotecas

* [Koin 2](https://insert-koin.io/) — Injeção de dependências
* [Jetpack](https://developer.android.com/jetpack/androidx) — Suíte de bibliotecas e ferramentas oficiais do Android
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/) — Componentes lifecycle aware como LiveData, ViewModel e outros
* [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) — Programação assíncrona e não bloqueante
* [Retrofit 2](http://square.github.io/retrofit/) — Requisições HTTP
* [Glide](https://bumptech.github.io/glide/) — Exibição e cache de imagens remotas

[News API]: https://newsapi.org/
