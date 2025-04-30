package no.uio.ifi.in2000_gruppe3.data.database
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `log_table` (
                `username` TEXT NOT NULL,
                `logHikeId` INTEGER NOT NULL DEFAULT 0,
                `times_walked` INTEGER NOT NULL DEFAULT 0,
                'notes' TEXT NOT NULL DEFAULT '',
                PRIMARY KEY(`username`, `hike_id`),
                FOREIGN KEY(`username`) REFERENCES `user_table`(`username`)
                ON UPDATE CASCADE ON DELETE CASCADE
            )
            """
        )
    }
}