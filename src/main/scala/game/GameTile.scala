package game

class GameTile(var tileType: Character = ' ', var display: Character = '-', val x: Integer, val y: Integer) {

    def revealTile(): Unit = {
        this.display = this.tileType
    }
}
