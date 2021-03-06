package eu.emundo.gradle.sevenz

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry
import org.apache.commons.compress.archivers.sevenz.SevenZFile
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

/**
 * Gradle Task whic extracts a 7z archive.
 */
class UnSevenZ extends DefaultTask {

    @InputFile
    File sourceFile

    @OutputDirectory
    File outputDir

    @TaskAction
    void extract(IncrementalTaskInputs inputs) {
        SevenZFile sevenZFile = new SevenZFile(sourceFile)
        SevenZArchiveEntry entry
        while ((entry = sevenZFile.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                continue
            }
            File curfile = new File(outputDir, entry.getName())
            File parent = curfile.getParentFile()
            if (parent != null && !parent.exists()) {
                parent.mkdirs()
            }
            FileOutputStream out = new FileOutputStream(curfile)
            byte[] content = new byte[(int) entry.getSize()]
            sevenZFile.read(content, 0, content.length)
            out.write(content)
            out.close()
        }
    }

}
