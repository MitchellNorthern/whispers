package game

class GameTile(var tileType: Character = ' ', var display: Character = '-', var revealed: Boolean = false) {

    def revealTile(): Unit = {
        this.revealed = true
    }

    def changeDisplayTo(c: Character): Unit = {
        this.display = c
    }
}
