package tech.americandad.constants;

public class FileConstants {

    public static final String USER_IMAGE_PATH = "/user/image/";
    public static final String JPG_EXTENSION = "jpg";
    //variavel abaixo busca a propriedade definida no system para o diretório do projeto, 
    //se retornar null significa que não tem nenhuma propriedade presente
    public static final String USER_FOLDER = System.getProperty("user.home") + "/Documentos/americandad/user";
    public static final String DIRECTORY_CREATED = "Created directory for: ";
    public static final String DEFAULT_USER_IMAGE_PATH = "/user/imagem/perfil/";
    public static final String FILE_SAVED_IN_FILE_SYSTEM = "Saved file in file system by name: ";
    public static final String DOT = ".";
    public static final String FORWARD_SLASH = "/";
    public static final String NOT_AN_IMAGE_FILE = " is not an image file. Please upload an image file";
    public static final String TEMP_PROFILE_IMAGE_BASE_URL = "https://robohash.org/";
    
}
