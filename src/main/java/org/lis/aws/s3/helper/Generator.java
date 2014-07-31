package org.lis.aws.s3.helper;

import java.io.File;
import java.util.List;

import org.lis.aws.s3.entity.Source;

/**
 * Generates the requests to be processed
 */
public class Generator
{
	private Tracker tracker;
	
	public Generator(final Tracker tracker)
	{
		this.tracker = tracker;
	}
	
	public void generate(String dirPath)
	{
		File[] files = new File(dirPath).listFiles();
		generate(files, dirPath);
	}
	
	private void generate(File[] files, String dirPath)
	{
		if(null != files && 0 < files.length)
		{
			for(File file : files)
			{
				if(file.isDirectory())
				{
					generate(file.listFiles(), dirPath);
				}
				else
				{
					tracker.add(translate(file, dirPath));
				}
			}
		}
	}
	
	private Source translate(File file, String dirPath)
	{
		Source source = new Source();
		source.setAbsolutePath(file.getAbsolutePath());
		source.setFileName(file.getName());
		// Calculate keyPath as [FullPath - dirPath]
		int slashCount = dirPath.charAt(dirPath.length() - 1) == '/' ? 0 : 1;
		String keyPath = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf(dirPath) + slashCount + dirPath.length());
		source.setKeyPath(keyPath);
		return source;
	}
	
	public void generate(Tracker tracker, List<Source> sources)
	{
		for(Source source : sources)
		{
			tracker.add(source);
		}
	}
}
