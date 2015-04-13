package bo.edu.ucbcba.mobileucb;

/**
 * Created by Richard Canoa on 07/04/2015.
 */
public class cls_notificaciones {
    private int _id_notificacion;
    private int _tipo_notificacion;
    private String _desc_tipo_notificacion;
    private String _aviso;
    private String _detalle;
    private String _rutaImagen;

    public cls_notificaciones() {
        this.set_tipo_notificacion(0);
        this.set_aviso("");
        this.set_detalle("");
        this.set_rutaImagen("");
    }

    public cls_notificaciones(int id_notificacion, int tipo_notificacion, String desc_tipo_notificacion,
                              String aviso, String detalle) {
        this.set_id_notificacion(id_notificacion);
        this.set_tipo_notificacion(tipo_notificacion);
        this.set_desc_tipo_notificacion(desc_tipo_notificacion);
        this.set_aviso(aviso);
        this.set_rutaImagen("");
    }

    public cls_notificaciones(int id_notificacion, int tipo_notificacion, String desc_tipo_notificacion,
                              String aviso, String detalle, String rutaImagen) {
        this.set_id_notificacion(id_notificacion);
        this.set_tipo_notificacion(tipo_notificacion);
        this.set_desc_tipo_notificacion(desc_tipo_notificacion);
        this.set_aviso(aviso);
        this.set_rutaImagen(rutaImagen);
    }


    public int get_id_notificacion() {
        return _id_notificacion;
    }

    public void set_id_notificacion(int _id_notificacion) {
        this._id_notificacion = _id_notificacion;
    }

    public int get_tipo_notificacion() {
        return _tipo_notificacion;
    }

    public void set_tipo_notificacion(int _tipo_notificacion) {
        this._tipo_notificacion = _tipo_notificacion;
    }

    public String get_desc_tipo_notificacion() {
        return _desc_tipo_notificacion;
    }

    public void set_desc_tipo_notificacion(String _desc_tipo_notificacion) {
        this._desc_tipo_notificacion = _desc_tipo_notificacion;
    }

    public String get_aviso() {
        return _aviso;
    }

    public void set_aviso(String _aviso) {
        this._aviso = _aviso;
    }

    public String get_detalle() {
        return _detalle;
    }

    public void set_detalle(String _detalle) {
        this._detalle = _detalle;
    }

    public String get_rutaImagen() {
        return _rutaImagen;
    }

    public void set_rutaImagen(String _rutaImagen) {
        this._rutaImagen = _rutaImagen;
    }
}
