package brownout;

/**
 * Execute compose files to deploy services
 * @author minxianx
 *
 */
public class ComposeFileExecution extends CommandExecution{

	public void initialDeploymentByComposeFile(){
		System.out.println("######Initial Deployment From Compose File######");
		//String command = "C:\\Users\\Administer\\git\\repository\\BrownoutModified\\src\\brownout\\initialDeployment.sh";
		String command = "initialDeployment.sh";
		executeCommandWithLessInfo(command);
	}
	
	public void updateDeploymentByComposeFile(){
		System.out.println("######Update Deployment From Compose File######");
		//String command = "C:\\Users\\Administer\\git\\repository\\BrownoutModified\\src\\brownout\\updateDeployment.sh";
		String command = "updateDeployment.sh";
		executeCommandWithLessInfo(command);
	}
}
