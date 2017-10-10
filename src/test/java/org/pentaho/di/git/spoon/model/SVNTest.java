package org.pentaho.di.git.spoon.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.subversion.javahl.ISVNRepos;
import org.apache.subversion.javahl.SVNRepos;
import org.eclipse.jgit.lib.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.ui.repository.pur.repositoryexplorer.model.UIRepositoryObjectRevisions;

public class SVNTest {
  private VCS vcs;
  private ISVNRepos admin;
  private File rootServer;
  private File rootClient;

  @Before
  public void setUp() throws Exception {
    // Set up a central remote repository
    rootServer = File.createTempFile( "svn_test_", "_server" );
    rootServer.delete();
    rootServer.mkdirs();
    admin = new SVNRepos();
    admin.create( rootServer, true, false, null, ISVNRepos.FSFS );

    rootClient = File.createTempFile( "svn_test_", "_client" );
    rootClient.delete();
    vcs = spy( new SVN() );
    doNothing().when( vcs ).showMessageBox( anyString(), anyString() );
    vcs.cloneRepo( rootClient.getPath(), "file://" + rootServer.getPath() );
    vcs.openRepo( rootClient.getPath() );
  }

  @After
  public void tearDown() throws Exception {
    admin.dispose();
    FileUtils.deleteDirectory( rootServer );
    FileUtils.deleteDirectory( rootClient );
  }

  @Test
  public void testCommit() throws Exception {
    UIRepositoryObjectRevisions revisions = vcs.getRevisions();
    File file = new File( rootClient.getPath(), "test.txt" );
    FileUtils.write( file, "Hello World" );
    vcs.add( "test.txt" );
    assertEquals( vcs.getRevisions().size(), revisions.size() + 1 );
    revisions = vcs.getRevisions();
    assertEquals( VCS.WORKINGTREE, revisions.get( 0 ).getName() );

    vcs.resetPath( "test.txt" );
    vcs.add( "test.txt" );

    vcs.commit( "user", "message" );
    assertEquals( revisions.size(), vcs.getRevisions().size() );
    revisions = vcs.getRevisions();
    assertEquals( "1", revisions.get( revisions.size() - 1 ).getName() );

    List<UIFile> files = vcs.getStagedFiles( "0", "1" );
    assertEquals( "test.txt", files.get( 0 ).getName() );

    InputStream inputStream = vcs.open( "test.txt", "1" );
    StringWriter writer = new StringWriter();
    IOUtils.copy( inputStream, writer, "UTF-8" );
    assertEquals( "Hello World", writer.toString() );

    inputStream = vcs.open( "test.txt", Constants.HEAD );
    writer = new StringWriter();
    IOUtils.copy( inputStream, writer, "UTF-8" );
    assertEquals( "Hello World", writer.toString() );

    inputStream = vcs.open( "test.txt", IVCS.WORKINGTREE );
    writer = new StringWriter();
    IOUtils.copy( inputStream, writer, "UTF-8" );
    assertEquals( "Hello World", writer.toString() );

    String diff = vcs.diff( "0", "1", "test.txt" );
    assertTrue( diff.contains( "+Hello World" ) );
  }

  @Test
  public void testPull() throws Exception {
    vcs.pull();
  }

  @Test
  public void testGetRemote() {
    String remote = vcs.getRemote();
    assertNotNull( remote );
  }
}
