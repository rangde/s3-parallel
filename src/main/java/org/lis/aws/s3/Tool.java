package org.lis.aws.s3;

import org.lis.aws.s3.command.S3Command;
import org.lis.aws.s3.command.S3DirPut;

public class Tool
{
	public static void main(String[] args)
	{
		if(null == args || args.length == 0 || !"PUT".equals(args[0]))
		{
			System.out.println("Invalid Arguments. PUT is the only supported mode.");
		}
		else
		{
			S3Command putDirCommand = new S3DirPut();
			putDirCommand.process(args);
		}
	}
}
