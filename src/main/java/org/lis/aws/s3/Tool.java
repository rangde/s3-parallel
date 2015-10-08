package org.lis.aws.s3;

import org.lis.aws.s3.command.S3Command;
import org.lis.aws.s3.command.S3DeleteAll;
import org.lis.aws.s3.command.S3DirPut;

public class Tool
{
	public static void main(String[] args)
	{
		if(null == args || args.length == 0 ||
            ( !"PUT".equals(args[0]) && !"DELETE_ALL".equals(args[0])))
		{
			System.out.println("Invalid Arguments. PUT and DELETE_ALL are the only supported modes.");
		}
		else
		{
			if("PUT".equals(args[0]))
			{
				S3Command putDirCommand = new S3DirPut();
				putDirCommand.process(args);
			}
			else if("DELETE_ALL".equals(args[0]))
			{
				S3Command deleteAllCommand = new S3DeleteAll();
				deleteAllCommand.process(args);
			}
		}
	}
}
