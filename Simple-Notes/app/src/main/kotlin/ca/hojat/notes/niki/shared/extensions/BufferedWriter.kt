package ca.hojat.notes.niki.shared.extensions

import java.io.BufferedWriter


fun BufferedWriter.writeLn(line: String) {
    write(line)
    newLine()
}
