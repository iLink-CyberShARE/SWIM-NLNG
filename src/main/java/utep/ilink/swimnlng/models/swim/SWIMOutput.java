package utep.ilink.swimnlng.models.swim;

import java.util.List;
import javax.validation.constraints.NotNull;

public class SWIMOutput {
    public String modelID;
    @NotNull
    public String varName;
    public List<VarInfo> varinfo;
    public Object varBenchMarks;
    @NotNull
    public Object varValue;
}
