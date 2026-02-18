# ArcaneTech - Aetherforge

**Aetherforge: A Revolução Magitech** é um mod para Minecraft que combina magia e tecnologia através da energia Aetherium.

## 🚀 Tutorial: Como criar o JAR no Termux

Este guia explica como baixar o código do GitHub e compilar o arquivo JAR do mod diretamente no seu dispositivo Android usando o Termux.

### 1. Preparar o Termux

Primeiro, certifique-se de que o Termux está atualizado e com os pacotes necessários instalados:

```bash
pkg update && pkg upgrade
pkg install git openjdk-21 -y
```

### 2. Clonar o Repositório

Para clonar o repositório, use o seguinte comando:

```bash
git clone https://github.com/davivida661-collab/aetherforge-mod.git
cd NOME_DO_REPO
```

### 3. Dar permissão ao Gradlew

O arquivo `gradlew` precisa de permissão de execução para rodar:

```bash
chmod +x gradlew
```

### 4. Compilar o JAR

Agora, execute o comando para compilar o mod. Isso pode demorar alguns minutos na primeira vez, pois o Gradle baixará todas as dependências do NeoForge.

```bash
./gradlew build
```

### 5. Localizar o arquivo JAR

Após a conclusão (BUILD SUCCESSFUL), o arquivo JAR estará na pasta `build/libs/`:

```bash
ls build/libs/
```

O arquivo que você deve usar é o que termina com `.jar` (geralmente `arcanetech-1.0.0.jar`).

---

## 🛠️ Comandos do Mod

Dentro do jogo, você pode usar os seguintes comandos:

- `/arcanetech help`: Mostra a ajuda do mod.
- `/arcanetech info`: Informações sobre o mod.
- `/arcanetech energy`: Informações sobre o sistema de energia.
- `/arcanetech give core`: Recebe um Aether Core (Requer Admin).
- `/arcanetech give rune`: Recebe uma Fire Rune (Requer Admin).

---

## 📝 Notas de Desenvolvimento

- **Versão do Minecraft:** 1.21.1
- **Loader:** NeoForge (21.1.80)
- **Java:** 21
- **Autor:** PlayerGames
