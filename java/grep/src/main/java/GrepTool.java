import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Grep Tool Emulator
 */
public class GrepTool {

    private enum GREP_OPTIONS {
        NONE( "" ),
        PRINT_LINE_NUMBER( "-n" ),
        NAMES_OF_FILES( "-l" ),
        CASE_INSENSITIVE( "-i" ),
        INVERT_MATCHING( "-v" ),
        ONLY_MATCH_ENTIRE_LINES( "-x" );

        private final String OPTIONS;

        GREP_OPTIONS( String options ) {
            this.OPTIONS = options;
        }

        static List<GREP_OPTIONS> extractOptions( List<String> options ) {
            List<GREP_OPTIONS> grepOptions = new ArrayList<>();

            if ( options != null && !options.isEmpty() ) {
                for ( GREP_OPTIONS a : GREP_OPTIONS.values() ) {
                    if ( options.contains( a.OPTIONS ) ) {
                        grepOptions.add( a );
                    }
                }
            }

            if ( grepOptions.isEmpty() ) {
                grepOptions.add( NONE );
            }

            return grepOptions;
        }
    }

    /**
     * File Opening helper
     *
     * @param fileName that needs to be opened
     * @throws java.io.IOException if file doesn't exist
     */
    private String[] fileReadHelper( String fileName ) throws IOException {
        Path file = Paths.get( fileName );
        String fileContents = Files.readString( file, StandardCharsets.UTF_8 );

        return fileContents.split( "\n" );
    }

    private String returnMatches( String[] fileContents, List<GREP_OPTIONS> grepOptions, String pattern ) {
        StringBuilder stringBuilder = new StringBuilder();

        for ( GREP_OPTIONS grepOption : grepOptions ) {
            switch ( grepOption ) {
                case NONE:
                default: {
                    for ( String a : fileContents ) {
                        if ( !a.isBlank() && a.contains( pattern ) ) {
                        stringBuilder.append( a, 0, a.length() - 1 );
                        }
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Returns the matched patterns
     *
     * @param pattern   to be matched
     * @param options   grep options
     * @param fileNames to be scanned
     */
    public String grep( String pattern, List<String> options, List<String> fileNames ) {
        List<GREP_OPTIONS> grepOptions = GREP_OPTIONS.extractOptions( options );
        StringBuilder stringBuilder = new StringBuilder();

        for ( String fileName : fileNames ) {
            String[] fileContents;
            try {
                fileContents = this.fileReadHelper( fileName );
                stringBuilder.append( this.returnMatches( fileContents, grepOptions, pattern ) );
            } catch ( IOException e ) {
                e.printStackTrace();
                return "";
            }
        }
        return stringBuilder.toString();
    }


}