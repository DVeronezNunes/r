package sfwinstaladorscript.comparabase.objects;

import java.util.List;

public class BaseCompare {

    private String _stringVersao;
    private List<Column> _listColumn;
    private List<Constraint> _listConstraint;
    private List<Function> _listFunction;
    private List<Index> _listIndex;
    private List<Package> _listPackage;
    private List<Package_Body> _listPackage_Body;
    private List<Procedure> _listProcedure;
    private List<Sequence> _listSequence;
    private List<Table> _listTable;
    private List<Trigger> _listTrigger;
    private List<View> _listView;

    /**
     * 
     */
    public BaseCompare() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Retorna a versão da comparação;
     *
     * @return String
     */
    public String get_stringVersao() {
        return _stringVersao;
    }

    /**
     * Seta a versão da comparação;
     *
     * @param _stringVersao
     */
    public void set_stringVersao(String _stringVersao) {
        this._stringVersao = _stringVersao;
    }

    /**
     * Retorna a lista de coluna;
     *
     * @return List<Column>;
     */
    public List<Column> get_listColumn() {
        return _listColumn;
    }

    /**
     * Seta a lista de coluna;
     *
     * @param _listColumn
     */
    public void set_listColumn(List<Column> _listColumn) {
        this._listColumn = _listColumn;
    }

    /**
     *
     * @return
     */
    public List<Constraint> get_listConstraint() {
        return _listConstraint;
    }

    /**
     *
     * @param _listConstraint
     */
    public void set_listConstraint(List<Constraint> _listConstraint) {
        this._listConstraint = _listConstraint;
    }

    /**
     *
     * @return
     */
    public List<Function> get_listFunction() {
        return _listFunction;
    }

    /**
     *
     * @param _listFunction
     */
    public void set_listFunction(List<Function> _listFunction) {
        this._listFunction = _listFunction;
    }

    /**
     *
     * @return
     */
    public List<Index> get_listIndex() {
        return _listIndex;
    }

    /**
     *
     * @param _listIndex
     */
    public void set_listIndex(List<Index> _listIndex) {
        this._listIndex = _listIndex;
    }

    /**
     *
     * @return
     */
    public List<Package> get_listPackage() {
        return _listPackage;
    }

    /**
     *
     * @param _listPackage
     */
    public void set_listPackage(List<Package> _listPackage) {
        this._listPackage = _listPackage;
    }

    /**
     *
     * @return
     */
    public List<Procedure> get_listProcedure() {
        return _listProcedure;
    }

    /**
     *
     * @param _listProcedure
     */
    public void set_listProcedure(List<Procedure> _listProcedure) {
        this._listProcedure = _listProcedure;
    }

    /**
     *
     * @return
     */
    public List<Sequence> get_listSequence() {
        return _listSequence;
    }

    /**
     *
     * @param _listSequence
     */
    public void set_listSequence(List<Sequence> _listSequence) {
        this._listSequence = _listSequence;
    }

    /**
     *
     * @return
     */
    public List<Table> get_listTable() {
        return _listTable;
    }

    /**
     *
     * @param _listTable
     */
    public void set_listTable(List<Table> _listTable) {
        this._listTable = _listTable;
    }

    /**
     *
     * @return
     */
    public List<Trigger> get_listTrigger() {
        return _listTrigger;
    }

    /**
     *
     * @param _listTrigger
     */
    public void set_listTrigger(List<Trigger> _listTrigger) {
        this._listTrigger = _listTrigger;
    }

    /**
     *
     * @return
     */
    public List<View> get_listView() {
        return _listView;
    }

    /**
     * 
     * @param _listView
     */
    public void set_listView(List<View> _listView) {
        this._listView = _listView;
    }

    /**
     * 
     * @return 
     */
    public List<Package_Body> get_listPackage_Body() {
        return _listPackage_Body;
    }

    /**
     * 
     */
    public void set_listPackage_Body(List<Package_Body> _listPackage_Body) {
        this._listPackage_Body = _listPackage_Body;
    }
}
