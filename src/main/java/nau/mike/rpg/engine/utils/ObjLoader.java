package nau.mike.rpg.engine.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nau.mike.rpg.engine.rendering.Vertex;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;

import java.net.URL;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.AIMesh.create;
import static org.lwjgl.assimp.Assimp.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ObjLoader {
  private static List<Float> positionList;
  private static List<Float> uvsList;
  private static List<Float> normalsList;
  private static List<Integer> indicesList;

  public static Vertex load(final String fileName) {
    ObjLoader.positionList = new ArrayList<>();
    ObjLoader.uvsList = new ArrayList<>();
    ObjLoader.normalsList = new ArrayList<>();
    ObjLoader.indicesList = new ArrayList<>();

    final String filePath = String.format("/models/%s.obj", fileName);
    final URL url = ObjLoader.class.getResource(filePath);
    if (url == null) {
      throw new IllegalStateException(String.format("Could not find file %s", filePath));
    }
    final AIScene aiScene =
        aiImportFile(
            url.getFile(),
            aiProcess_Triangulate
                | aiProcess_TransformUVCoords
                | aiProcess_FlipUVs
                | aiProcess_GenSmoothNormals
                | aiProcess_FixInfacingNormals
                | aiProcess_JoinIdenticalVertices);
    if (aiScene == null) {
      throw new IllegalStateException(String.format("Could not find model file %s", url.getPath()));
    }
    final PointerBuffer buffer = aiScene.mMeshes();
    if (buffer == null) {
      throw new IllegalStateException("There were no Meshes to load");
    }
    for (int i = 0; i < buffer.limit(); i++) {
      final AIMesh aiMesh = create(buffer.get(i));
      processMesh(aiMesh);
    }

    final float[] positions = MathUtil.toFloatArray(positionList);
    final float[] uvs = MathUtil.toFloatArray(uvsList);
    final float[] normals = MathUtil.toFloatArray(normalsList);
    final int[] indices = MathUtil.toIntArray(indicesList);

    aiReleaseImport(aiScene);
    return new Vertex(positions, normals, uvs, indices);
  }

  private static void processMesh(final AIMesh aiMesh) {
    final AIVector3D.Buffer vectors = aiMesh.mVertices();
    final AIVector3D.Buffer uvs = aiMesh.mTextureCoords(0);
    final AIVector3D.Buffer normals = aiMesh.mNormals();

    processBuffer(vectors, positionList, 3);
    processBuffer(uvs, uvsList, 2);
    processBuffer(normals, normalsList, 3);

    final AIFace.Buffer aiFaces = aiMesh.mFaces();
    while (aiFaces.remaining() > 0) {
      final AIFace faces = aiFaces.get();
      final IntBuffer buffer = faces.mIndices();
      while (buffer.remaining() > 0) {
        final int index = buffer.get();
        indicesList.add(index);
      }
    }
  }

  private static void processBuffer(
      final AIVector3D.Buffer buffer, final List<Float> list, final int size) {
    if (buffer != null) {
      while (buffer.remaining() > 0) {
        final AIVector3D normal = buffer.get();
        list.add(normal.x());
        list.add(normal.y());
        if (size == 3) {
          list.add(normal.z());
        }
      }
    }
  }
}
