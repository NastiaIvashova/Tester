import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;


public class Main {
    private static final String ACCESS_TOKEN = "A7NO0H6Krm0AAAAAAAAAAUmx3JtK0PN3O39APkAHbl7rXmWFNkmIKovhB9JSQoht";

    @Test
    public static void main (String[] args) throws DbxException, IOException
    {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/nastia").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        FullAccount account = client.users().getCurrentAccount();
        System.out.println(account.getName().getDisplayName());

        ListFolderResult result = client.files().listFolder( "");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }

        try (InputStream in = new FileInputStream("src/FileTXT/text.txt"))
        {
            FileMetadata metadata = client.files().uploadBuilder("/text.txt").uploadAndFinish(in);
        }

        client.files().deleteV2("/text.txt");
    }
}