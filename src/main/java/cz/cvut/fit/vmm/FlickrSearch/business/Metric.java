package cz.cvut.fit.vmm.FlickrSearch.business;


public enum Metric {
    CIE76(30),
    CIE2000(20),
    RGB(64);

    private final double same;

    private Metric(double same){
        this.same = same;
    }

    public double getSame(){
        return same;
    }
}
